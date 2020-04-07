package com.hb0730.boot.admin.project.system.post.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hb0730.boot.admin.commons.annotation.Log;
import com.hb0730.boot.admin.commons.constant.BusinessTypeEnum;
import com.hb0730.boot.admin.commons.constant.ModuleName;
import com.hb0730.boot.admin.commons.constant.SystemConstants;
import com.hb0730.boot.admin.commons.utils.PageInfoUtil;
import com.hb0730.boot.admin.commons.utils.bean.BeanUtils;
import com.hb0730.boot.admin.commons.web.controller.BaseController;
import com.hb0730.boot.admin.commons.web.response.ResponseResult;
import com.hb0730.boot.admin.commons.web.response.Result;
import com.hb0730.boot.admin.project.system.post.model.entity.SystemPostEntity;
import com.hb0730.boot.admin.project.system.post.model.vo.PostParams;
import com.hb0730.boot.admin.project.system.post.model.vo.SystemPostVO;
import com.hb0730.boot.admin.project.system.post.service.ISystemPostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.hb0730.boot.admin.commons.constant.RequestMappingNameConstants.REQUEST_POST;

/**
 * <p>
 * 系统岗位  前端控制器
 * </p>
 *
 * @author bing_huang
 * @since 2020-03-28
 */
@RestController
@RequestMapping(REQUEST_POST)
public class SystemPostController extends BaseController {
    @Autowired
    private ISystemPostService systemPostService;

    /**
     * <p>
     * 保存
     * </p>
     *
     * @param vo 岗位
     * @return 是否成功
     */
    @PostMapping("/save")
    @Log(paramsName = {"vo"}, module = ModuleName.POST, title = "岗位保存", businessType = BusinessTypeEnum.INSERT)
    public Result save(@RequestBody SystemPostVO vo) {
        SystemPostEntity entity = BeanUtils.transformFrom(vo, SystemPostEntity.class);
        QueryWrapper<SystemPostEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SystemPostEntity.NUMBER, vo.getNumber());
        int count = systemPostService.count(queryWrapper);
        if (count > 0) {
            return ResponseResult.resultFall("岗位编码已存在");
        }
        systemPostService.save(entity);
        return ResponseResult.resultSuccess("保存成功");
    }

    /**
     * <p>
     * 获取分页后的岗位
     * </p>
     *
     * @param page     页数
     * @param pageSize 数量
     * @param params   过滤参数
     * @return 分页后的岗位
     */
    @PostMapping("/all/{page}/{pageSize}")
    public Result getPostPage(@PathVariable Integer page, @PathVariable Integer pageSize, @RequestBody PostParams params) {
        PageHelper.startPage(page, pageSize);
        QueryWrapper<SystemPostEntity> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(params)) {
            if (StringUtils.isNotBlank(params.getName())) {
                queryWrapper.eq(SystemPostEntity.NAME, params.getName());
            }
            if (StringUtils.isNotBlank(params.getNumber())) {
                queryWrapper.eq(SystemPostEntity.NUMBER, params.getNumber());
            }
            if (Objects.nonNull(params.getIsEnabled())) {
                queryWrapper.eq(SystemPostEntity.IS_ENABLED, params.getIsEnabled());
            }
        }
        List<SystemPostEntity> entities = systemPostService.list(queryWrapper);
        PageInfo<SystemPostEntity> pageInfo = new PageInfo<>(entities);
        PageInfo<SystemPostVO> info = PageInfoUtil.toBean(pageInfo, SystemPostVO.class);
        return ResponseResult.resultSuccess(info);
    }

    /**
     * <p>
     * 获取岗位
     * </p>
     *
     * @param params 过滤条件
     * @return 岗位信息
     */
    @PostMapping("/all")
    public Result getPost(@RequestBody PostParams params) {
        QueryWrapper<SystemPostEntity> queryWrapper = new QueryWrapper<>();
        List<SystemPostEntity> entities = systemPostService.list(queryWrapper);
        List<SystemPostVO> vos = BeanUtils.transformFromInBatch(entities, SystemPostVO.class);
        return ResponseResult.resultSuccess(vos);
    }

    /**
     * <p>
     * 更新岗位
     * </p>
     *
     * @param id 岗位id
     * @param vo 岗位信息
     * @return 是否成功
     */
    @PostMapping("/update/{id}")
    @Log(paramsName = {"vo"}, module = ModuleName.POST, title = "岗位修改", businessType = BusinessTypeEnum.UPDATE)
    public Result updateById(@PathVariable Long id, @RequestBody SystemPostVO vo) {
        SystemPostEntity entity = systemPostService.getById(id);
        BeanUtils.updateProperties(vo, entity);
        systemPostService.updateById(entity);
        return ResponseResult.resultSuccess("修改成功");
    }

    /**
     * 删除
     *
     * @param id 岗位id
     * @return 是否成功
     */
    @GetMapping("/delete/{id}")
    @Log(module = ModuleName.POST, title = "岗位删除", businessType = BusinessTypeEnum.DELETE)
    public Result deleteById(@PathVariable Long id) {
        systemPostService.deleteById(id);
        return ResponseResult.resultSuccess("修改成功");
    }

    /**
     * <p>
     * 删除
     * </P>
     *
     * @param ids 岗位id
     * @return 是否成功
     */
    @PostMapping("/delete")
    @Log(module = ModuleName.POST, title = "岗位删除", businessType = BusinessTypeEnum.DELETE)
    public Result deleteByIds(@RequestBody List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            systemPostService.removeByIds(ids);
            return ResponseResult.resultSuccess("修改成功");
        }
        return ResponseResult.resultFall("请选择");
    }
}

