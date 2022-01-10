package com.daizhihua.tools.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 代志华
 * @since 2022-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageImage extends Model<MessageImage> {

    private static final long serialVersionUID=1L;

    private String messageId;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 平台生成的告警Id
     */
    private String pictureId;

    /**
     * 告警图片URL
     */
    private String pictureUrl;

    /**
     * 时间
     */
    private String pictureTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
