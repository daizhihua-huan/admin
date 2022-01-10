package com.daizhihua.mnt.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.mnt.entity.*;
import com.daizhihua.mnt.entity.vo.ShellConnectInfo;
import com.daizhihua.mnt.entity.vo.WebShellData;
import com.daizhihua.mnt.mapper.MntDeployMapper;
import com.daizhihua.mnt.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.mnt.util.JschUtil;
import com.daizhihua.mnt.util.SftpUtils;
import com.daizhihua.mnt.util.WebShellUtils;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static cn.hutool.system.SystemUtil.FILE_SEPARATOR;

/**
 * <p>
 * 部署管理 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-17
 */
@Slf4j
@Service
public class MntDeployServiceImpl extends ServiceImpl<MntDeployMapper, MntDeploy> implements MntDeployService {


    @Autowired
    private MntServerService mntServerService;

    @Autowired
    private MntAppService mntAppService;
    /**
     * 部署和服务器关联表
     */
    @Autowired
    private MntDeployServerService mntDeployServerService;

    @Autowired
    private MntDeployHistoryService mntDeployHistoryService;

    @Override
    public IPage<MntDeploy> page(Pageable pageable, QueryVo queryVo) {
        Page<MntDeploy>  page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<MntDeploy> queryWrapper = new QueryWrapper<>();
        Page<MntDeploy> pageList = this.page(page, queryWrapper);
        List<MntDeploy> records = pageList.getRecords();
        for (MntDeploy record : records) {
            List<MntServer> mntServerForDeployId = mntServerService.getMntServerForDeployId(record.getDeployId());
            record.setDeploys(mntServerForDeployId);
            record.setMntApp(mntAppService.getById(record.getAppId()));
        }
        return pageList;
    }

    @Override
    public boolean mySave(MntDeploy mntDeploy) {
        mntDeploy.setAppId(mntDeploy.getApp());
        mntDeploy.setCreateBy(SecurityUtils.getCurrentUsername());
        mntDeploy.setUpdateBy(SecurityUtils.getCurrentUsername());
        mntDeploy.setCreateTime(DateUtils.getDateTime());
        mntDeploy.setUpdateTime(DateUtils.getDateTime());
        boolean flag = this.save(mntDeploy);
        if(flag){
            /**
             * 新增服务器管理
             */
            List<MntServer> deploys = mntDeploy.getDeploys();
            for (MntServer deploy : deploys) {
                MntDeployServer mntDeployServer = new MntDeployServer();
                mntDeployServer.setServerId(deploy.getId());
                mntDeployServer.setDeployId(mntDeploy.getDeployId());
                mntDeployServerService.save(mntDeployServer);
            }

        }
        return flag;
    }

    @Override
    public boolean myUpdate(MntDeploy mntDeploy) {
        mntDeploy.setUpdateTime(DateUtils.getDateTime());
        mntDeploy.setUpdateBy(SecurityUtils.getCurrentUsername());
        mntDeploy.setAppId(mntDeploy.getApp());
        boolean flag = this.updateById(mntDeploy);
        if(flag){
            mntDeployServerService.deleteByDeployId(mntDeploy.getDeployId());
            List<MntServer> deploys = mntDeploy.getDeploys();
            for (MntServer deploy : deploys) {
                MntDeployServer mntDeployServer = new MntDeployServer();
                mntDeployServer.setDeployId(mntDeploy.getDeployId());
                mntDeployServer.setServerId(deploy.getId());
                mntDeployServerService.save(mntDeployServer);
            }
        }
        return flag;
    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean flag = this.removeByIds(ids);
        if(flag){
            for (Long id : ids) {
                mntDeployServerService.deleteByDeployId(id);
            }
        }

        return flag;
    }

    /**
     * 参数判断
     * @param deployId
     * @param appId
     * @return
     */
    public Map<String,Object> judgeParams(Long deployId, Long appId){
        Map<String,Object> map = new HashMap<>();
        MntDeploy mntDeploy = this.getById(deployId);
        if(mntDeploy==null){
            throw new BadRequestException("当前没有部署的项目,请重新选择");
        }
        MntApp mntapp = mntAppService.getById(appId);
        if(mntapp==null){
            throw new BadRequestException("当前没有部署的应用");
        }
        List<MntServer> mntServerForDeploys = mntServerService.getMntServerForDeployId(deployId);
        if(mntServerForDeploys==null&&mntServerForDeploys.size()<=0){
            throw new BadRequestException("当前没有选择服务器");
        }
        map.put("mntDeploy",mntDeploy);
        map.put("mntapp",mntapp);
        map.put("mntServer",mntServerForDeploys);
        return map;
    }
    /**
     * 文件上传
     * @param file
     * @param deployId
     * @param appId
     */
    @Override
    public void upload(MultipartFile file, Long deployId, Long appId,Long userId) {

        /**
         * 开始部署
         * 1、stfp上传服务器
         * 2、执行服务脚本
         * 3、
         */
        Map<String, Object> map = judgeParams(deployId, appId);
        try {
            stfpUpload(file,(List<MntServer>) map.get("mntServer"),(MntApp) map.get("mntapp"),userId,deployId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(MntDeploy mntDeploy) {
        MntApp mntapp = mntDeploy.getMntApp();
        List<MntServer> mntServers =  mntDeploy.getDeploys();
        for (MntServer mntServer : mntServers) {
            stopApp(mntapp.getDeployPort(),getJschUtil(mntServer),SecurityUtils.getCurrentUserId());
        }
    }

    /**
     * 启动 app
     * @param mntDeploy
     */
    @Override
    public void start(MntDeploy mntDeploy) {
        MntApp mntApp = mntDeploy.getMntApp();
        List<MntServer> deploys = mntDeploy.getDeploys();
        for (MntServer deploy : deploys) {
            JschUtil jschUtil = getJschUtil(deploy);
            /**
             * 首先判断是否启动
             * 如果启动那么先停止
             * 然后再启动
             */
            if(checkStatus(jschUtil,mntApp.getDeployPort())){
                jschUtil = getJschUtil(deploy);
                stopApp(mntApp.getDeployPort(),jschUtil,SecurityUtils.getCurrentUserId());
            }
            jschUtil = getJschUtil(deploy);
            jschUtil.execute(mntApp.getStartScript(),SecurityUtils.getCurrentUserId());
        }
    }

    /**
     * 查询状态
     * @param mntDeploy
     */
    @Override
    public void status(MntDeploy mntDeploy) {
        List<MntServer> deploys = mntDeploy.getDeploys();
        for (MntServer deploy : deploys) {
            MntApp mntApp = mntDeploy.getMntApp();
            JschUtil jschUtil = getJschUtil(deploy);
            jschUtil.execute(String.format("fuser -n tcp %d", mntApp.getDeployPort()),SecurityUtils.getCurrentUserId());
        }
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        Page<MntDeploy>  page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<MntDeploy> records = this.page(page).getRecords();
        List<Map<String,Object>> list = new ArrayList<>();
        for (MntDeploy record : records) {
            List<MntServer> mntServers = mntServerService.getMntServerForDeployId(record.getDeployId());
            record.setDeploys(mntServers);
            record.setMntApp(mntAppService.getById(record.getAppId()));
            String name = "";
            for (MntServer mntServer : mntServers) {
                name+=mntServer.getName();
            }
            Map<String,Object> map = new HashMap<>();
            map.put("应用名称",record.getMntApp().getName());
            map.put("服务器列表",name);
            map.put("部署日期",record.getCreateTime());
            list.add(map);
        }
    }

    /**
     * 停App
     *
     * @param port 端口
     * @param executeShellUtil /
     */
    private void stopApp(int port, JschUtil executeShellUtil,Long userId) {
        //发送停止命令
        executeShellUtil.execute(String.format("lsof -i :%d|grep -v \"PID\"|awk '{print \"kill -9\",$2}'|sh", port),userId);
    }


    private Boolean checkStatus(JschUtil jschUtil,int port){
        String result = jschUtil.executeForResult(String.format("fuser -n tcp %d", port));
        return result.indexOf("/tcp:")>0;
    }

    /**
     * 通过服务器获取连接信息
     * @param mntServer
     * @return
     */
    private WebShellData getWebSellDataForMntServer(MntServer mntServer){
        WebShellData sshData = new WebShellData();
        sshData.setHost(mntServer.getIp());
        sshData.setPort(mntServer.getPort());
        sshData.setPassword(mntServer.getPassword());
        sshData.setUsername(mntServer.getAccount());
        sshData.setOperate("upload");
        return sshData;
    }

    private JschUtil getJschUtil(MntServer mntServer){
        WebShellData webSellDataForMntServer = getWebSellDataForMntServer(mntServer);
        JschUtil jschUtil = new JschUtil(webSellDataForMntServer);
        return jschUtil;
    }


    /**
     * 上传
     * @param file
     * @param list
     * @param mntApp
     * @param userId
     * @throws IOException
     * @throws SftpException
     */
    private void stfpUpload(MultipartFile file,List<MntServer> list,MntApp mntApp,Long userId,Long deployId) throws IOException, SftpException {
        for (MntServer mntServer : list) {
            WebShellData sshData = getWebSellDataForMntServer(mntServer);
            SftpUtils sftpUtils = new SftpUtils(sshData);
            if (sftpUtils.login()) {
                sftpUtils.upload(mntApp.getUploadPath(), file.getOriginalFilename(), file.getInputStream(),file.getSize(),userId);
                sftpUtils.logout();
                //连接服务器

                getJschUtil(mntServer).execute("mkdir -p " + mntApp.getUploadPath(),userId);
                getJschUtil(mntServer).execute("mkdir -p " + mntApp.getBackupPath(),userId);
                getJschUtil(mntServer).execute("mkdir -p " + mntApp.getDeployPath(),userId);
                if(checkStatus( getJschUtil(mntServer),mntApp.getDeployPort())){
                    stopApp(mntApp.getDeployPort(),getJschUtil(mntServer),SecurityUtils.getCurrentUserId());
                    /**
                     * 部署
                     */
                    backupApp(getJschUtil(mntServer),mntServer.getIp(),mntApp.getDeployPath()+FILE_SEPARATOR,
                            mntApp.getName(),mntApp.getBackupPath()+FILE_SEPARATOR, deployId);
                }
                getJschUtil(mntServer).execute(mntApp.getStartScript(),userId);

            }
        }
    }

    private void backupApp(JschUtil jschUtil, String ip, String fileSavePath, String appName, String backupPath, Long id) {

        String deployDate = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        StringBuilder sb = new StringBuilder();
        backupPath += appName + FILE_SEPARATOR + deployDate + "\n";
        sb.append("mkdir -p ").append(backupPath);
        sb.append("mv -f ").append(fileSavePath);
        sb.append(appName).append(" ").append(backupPath);
        log.info("备份应用脚本:" + sb.toString());
        jschUtil.execute(sb.toString(),SecurityUtils.getCurrentUserId());
        //还原信息入库
        MntDeployHistory deployHistory = new MntDeployHistory();
        deployHistory.setAppName(appName);
        deployHistory.setDeployUser(SecurityUtils.getCurrentUsername());
        deployHistory.setIp(ip);
        deployHistory.setDeployId(id);
        mntDeployHistoryService.save(deployHistory);
    }
}
