package com.digcoin.snapx.server.app.restaurant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.app.restaurant.dto.command.OrderCreateOrderCmd;
import com.digcoin.snapx.server.app.restaurant.service.AppOrderService;
import com.digcoin.snapx.server.app.restaurant.vo.OrderCreateOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:18
 * @description
 */
@Tag(name = "321 - 餐厅订单")
@SaCheckLogin
@RequiredArgsConstructor
@RestController
@RequestMapping("/ret/order")
public class AppOrderController {

    private final AppOrderService appOrderService;

    @Operation(summary = "创建订单")
    @PostMapping("create-order")
    public OrderCreateOrderVO createOrder(@Schema(hidden = true) CurrentUser currentUser, @Valid @RequestBody OrderCreateOrderCmd cmd) {
        return appOrderService.createOrder(cmd, currentUser);
    }

}
