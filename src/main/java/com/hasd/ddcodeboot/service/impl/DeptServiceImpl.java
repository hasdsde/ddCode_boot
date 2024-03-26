package com.hasd.ddcodeboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hasd.ddcodeboot.entity.Dept;
import com.hasd.ddcodeboot.mapper.DeptMapper;
import com.hasd.ddcodeboot.service.IDeptService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Resource
    DeptMapper deptMapper;


    @Override
    @Cacheable(cacheNames = "dept.page", key = "#currentPage + #pageSize + #deptId + #name+#description+#parentId")
    public Page<Dept> getPage(int currentPage, int pageSize, String deptId, String name, String description, String parentId) {
        Page<Dept> page = new Page<>(currentPage, pageSize);
        List<Dept> dept = deptMapper.getPage((currentPage - 1) * pageSize, pageSize, deptId, name, description, parentId);
        Integer total = deptMapper.getPageCount(deptId);
        page.setTotal(total);
        page.setRecords(dept);
        return page;
    }

    @Override
    @Cacheable(cacheNames = "dept.page", key = "#deptId")
    public List<Dept> getDeptByDeptId(String deptId) {
        return deptMapper.getDeptByDeptId(deptId);
    }


    //缓存重复问题
    //[Dept(id=6, parentId=1, name=部门1, description=部门1, createdAt=null, deletedAt=null, updatedAt=null), Dept(id=46, parentId=6, name=部门1的子类, description=null, createdAt=null, deletedAt=null, updatedAt=null)]
    @Cacheable(cacheNames = "dept.id", key = "#deptId")
    public List<Integer> getDeptIdByDeptId(String deptId) {
        return deptMapper.getDeptIdByDeptId(deptId);
    }
}
