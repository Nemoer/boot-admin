package com.hb0730.boot.admin.domain.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 分录
 *
 * @author bing_huang
 * @since 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class BusinessEntryDomain extends BaseDomain {
    /**
     * id
     */
    @TableField(value = "id")
    private Long id;
    /**
     * 父id
     */
    @TableField(value = "parent_id")
    private Long parentId;
}