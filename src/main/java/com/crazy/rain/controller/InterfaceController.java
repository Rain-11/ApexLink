package com.crazy.rain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crazy.rain.annotation.AuthCheck;
import com.crazy.rain.common.BaseResponse;
import com.crazy.rain.common.DeleteRequest;
import com.crazy.rain.common.ErrorCode;
import com.crazy.rain.common.ResultUtil;
import com.crazy.rain.converter.InterfaceConverter;
import com.crazy.rain.exception.ThrowUtils;
import com.crazy.rain.model.dto.interface_info.InterfaceAddDto;
import com.crazy.rain.model.dto.interface_info.InterfaceQueryDto;
import com.crazy.rain.model.dto.interface_info.InterfaceUpdateDto;
import com.crazy.rain.model.entity.InterfaceInfo;
import com.crazy.rain.model.entity.User;
import com.crazy.rain.model.vo.InterfaceInfoVo;
import com.crazy.rain.service.InterfaceInfoService;
import com.crazy.rain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/interface")
@Slf4j
@AllArgsConstructor
@Tag(name = "接口信息控制器")
public class InterfaceController {


    private final InterfaceInfoService interfaceInfoService;
    private final InterfaceConverter interfaceConverter;
    private final UserService userService;

    @PostMapping("/addInterface")
    @Operation(summary = "添加接口")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Void> addInterface(@RequestBody InterfaceAddDto interfaceAddDto) {
        ThrowUtils.throwIf(interfaceAddDto == null, ErrorCode.NOT_FOUND_ERROR, "请求参数不存在");
        interfaceInfoService.addInterface(interfaceAddDto);
        return ResultUtil.success();
    }


    @DeleteMapping("/deleteInterfaceBasedOnID")
    @Operation(summary = "删除接口")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> deleteInterfaceBasedOnID(DeleteRequest deleteRequest) {
        return ResultUtil.success(interfaceInfoService.removeById(deleteRequest.getId()));
    }

    @DeleteMapping("/batchDeletion")
    @Operation(summary = "批量删除接口")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> batchDeletion(@RequestParam List<Long> ids) {
        return ResultUtil.success(interfaceInfoService.removeByIds(ids));
    }

    @PutMapping("/modifyInterfaceInformation")
    @Operation(summary = "修改接口信息")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> modifyInterfaceInformation(@RequestBody InterfaceUpdateDto interfaceUpdateDto) {
        ThrowUtils.throwIf(interfaceUpdateDto == null || interfaceUpdateDto.getId() == null,
                ErrorCode.NOT_FOUND_ERROR, "请求参数为空");

        return ResultUtil.success(interfaceInfoService.modifyInterfaceInformation(interfaceUpdateDto));
    }

    @GetMapping("/queryInterfaceBasedOnID/{id}")
    @Operation(summary = "根据id获取接口信息")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<InterfaceInfoVo> queryInterfaceBasedOnID(@PathVariable("id") String id) {
        return ResultUtil.success(interfaceConverter.interfaceInfoVoConvert(interfaceInfoService.getById(id)));
    }

    @PostMapping("/pagingQueryInterfaceInformation")
    @Operation(summary = "分页查询接口信息")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Page<InterfaceInfoVo>> pagingQueryInterfaceInformation(@RequestBody InterfaceQueryDto interfaceQueryDto) {
        ThrowUtils.throwIf(interfaceQueryDto == null, ErrorCode.NOT_FOUND_ERROR, "请求参数为空");
        int pageSize = interfaceQueryDto.getPageSize();
        int current = interfaceQueryDto.getCurrent();
        if (current <= 0) {
            current = 1;
        }
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.SYSTEM_ERROR, "每页显示条数超20");
        IPage<InterfaceInfo> page = new Page<>(current, pageSize);
        interfaceInfoService.page(page, interfaceInfoService.getQueryWrapper(interfaceQueryDto));
        List<InterfaceInfo> interfaceInfoList = page.getRecords();
        List<InterfaceInfoVo> interfaceInfoVos = new ArrayList<>();
        if (interfaceInfoList != null && !interfaceInfoList.isEmpty()) {
            List<Long> userIds = interfaceInfoList.stream().map(InterfaceInfo::getUserId).collect(Collectors.toList());
            if (!userIds.isEmpty()) {
                List<User> users = userService.listByIds(userIds);
                interfaceInfoVos = interfaceConverter.interfaceInfoVoConvert(interfaceInfoList);
                interfaceInfoVos.forEach(interfaceInfoVo -> users.forEach(user -> {
                    if (interfaceInfoVo.getUserId().equals(user.getId())) {
                        interfaceInfoVo.setUserName(user.getUserName());
                    }
                }));
            }
        }
        return ResultUtil.success(new Page<InterfaceInfoVo>(current, pageSize, page.getTotal())
                .setRecords(interfaceInfoVos));
    }


}
