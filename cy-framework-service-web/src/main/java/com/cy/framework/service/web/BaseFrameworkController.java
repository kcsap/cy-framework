package com.cy.framework.service.web;

import com.cy.framework.service.dao.BaseFrameworkService;
import com.cy.framework.util.result.ResultParam;
import com.cy.framework.util.result.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公用的工厂模式
 *
 * @param <T>
 */
public abstract class BaseFrameworkController<T, ID> {
    private BaseFrameworkService baseService;

    public abstract void init();

    public final void setBaseService(BaseFrameworkService service) {
        baseService = service;
    }

    /**
     * @Description: 添加数据
     * @author yangchengfu
     * @Date : 2017/7/28 9:42
     */
    @ApiOperation(value = "添加", httpMethod = "POST", notes = "添加")
    @PostMapping({"insert"})
    public ResultParam insert(@RequestBody @ApiParam(name = "添加对象", value = "添加对象", required = true) T map, HttpServletRequest request, HttpServletResponse response) {
        init();
        return baseService.insert(map, request, response);
    }

    /**
     * @Description: 删除单条数据
     * @author yangchengfu
     * @Date : 2017/7/28 9:43
     */
    @ApiOperation(value = "根据ID删除", httpMethod = "GET", notes = "根据ID删除")
    @ApiImplicitParam(value = "ID", name = "id", required = true)
    @GetMapping("delete")
    public ResultParam delete(@RequestParam ID id, HttpServletRequest request, HttpServletResponse response) {
        init();
        return baseService.delete(id, request, response);
    }

    /**
     * 修改
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "更新", httpMethod = "POST", notes = "更新")
    @PostMapping({"update"})
    public ResultParam update(@RequestBody @ApiParam(name = "修改的对象", value = "传入JSON格式数据", required = true) T map, HttpServletRequest request, HttpServletResponse response) {
        init();
        return baseService.update(map, request, response);
    }

    /**
     * 查询单条信息
     *
     * @param id
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "查询单条信息", httpMethod = "GET", notes = "根据ID查询单条信息")
    @ApiImplicitParam(value = "ID", name = "id", required = true, paramType = "query")
    @GetMapping("findById")
    public ResultParam findById(@RequestParam ID id, HttpServletRequest request, HttpServletResponse response) {
        init();
        return ResultUtil.success(baseService.findById(id, request, response));
    }

    /**
     * 查询
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "查询", httpMethod = "GET", notes = "查询")
    @GetMapping("query")
    public ResultParam query(T map, HttpServletRequest request, HttpServletResponse response) {
        init();
        return ResultUtil.success(baseService.query(map, request, response));
    }

    /**
     * 分页查询
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "分页查询", httpMethod = "GET", notes = "分页查询")
    @GetMapping("queryListPage")
    public ResultParam queryListPage(T map, HttpServletRequest request, HttpServletResponse response) {
        init();
        return ResultUtil.success(baseService.queryListPage(map, request, response));
    }

    /**
     * 查询总条数
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    public ResultParam queryListPageCount(T map, HttpServletRequest request, HttpServletResponse response) {
        init();
        return ResultUtil.success(baseService.queryListPageCount(map, request, response));
    }

    /**
     * PC端/后台管理分页查询
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "PC分页查询", httpMethod = "GET", notes = "PC分页查询")
    @GetMapping("queryManagerListPage")
    public ResultParam queryManagerListPage(T map,
                                            HttpServletRequest request, HttpServletResponse response) {
        init();
        return ResultUtil.success(baseService.queryManagerListPage(map, request, response));
    }
}
