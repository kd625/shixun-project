package com.ruoyi.web.controller.virtual;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.virtualdomain.VtCourse;
import com.ruoyi.system.service.virtualservice.IVtCourseService;

/**
 * 课程Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/virtual/course")
public class VtCourseController extends BaseController
{
    @Autowired
    private IVtCourseService courseService;

    @PreAuthorize("@ss.hasPermi('virtual:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtCourse course)
    {
        startPage();
        List<VtCourse> list = courseService.selectVtCourseList(course);
        return getDataTable(list);
    }

    @Log(title = "课程", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:course:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtCourse course)
    {
        List<VtCourse> list = courseService.selectVtCourseList(course);
        ExcelUtil<VtCourse> util = new ExcelUtil<VtCourse>(VtCourse.class);
        util.exportExcel(response, list, "课程数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:course:query')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable("courseId") Long courseId)
    {
        return success(courseService.selectVtCourseById(courseId));
    }

    @PreAuthorize("@ss.hasPermi('virtual:course:add')")
    @Log(title = "课程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VtCourse course)
    {
        course.setCreateBy(getUsername());
        return toAjax(courseService.insertVtCourse(course));
    }

    @PreAuthorize("@ss.hasPermi('virtual:course:edit')")
    @Log(title = "课程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VtCourse course)
    {
        course.setUpdateBy(getUsername());
        return toAjax(courseService.updateVtCourse(course));
    }

    @PreAuthorize("@ss.hasPermi('virtual:course:remove')")
    @Log(title = "课程", businessType = BusinessType.DELETE)
    @DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds)
    {
        return toAjax(courseService.deleteVtCourseByIds(courseIds));
    }

    /**
     * 获取Course选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return success(courseService.selectVtCourseList(new VtCourse()));
    }
}
