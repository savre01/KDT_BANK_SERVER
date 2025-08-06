package com.bank.server.controller.customer;

import com.bank.server.dto.customer.AccountRequestRequest;
import com.bank.server.dto.customer.AccountRequestResponse;
import com.bank.server.model.customer.AccountRequestEntity;
import com.bank.server.service.customer.AccountRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/account-request")
@RequiredArgsConstructor
public class AccountRequestController {

    private final AccountRequestService accountRequestService;

    @PostMapping
    public ResponseEntity<AccountRequestResponse> createRequest(@RequestBody AccountRequestRequest dto) {
        AccountRequestEntity request = accountRequestService.createRequest(dto);
        return ResponseEntity.ok(new AccountRequestResponse(request));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AccountRequestResponse>> getPendingRequests() {
        return ResponseEntity.ok(
                accountRequestService.getPendingRequests().stream()
                        .map(AccountRequestResponse::new)
                        .collect(toList())
        );
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        accountRequestService.processApproval(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        accountRequestService.processRejection(id);
        return ResponseEntity.ok().build();
    }
}