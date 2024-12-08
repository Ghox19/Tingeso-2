package com.microservice.tracingMicroservice.Controllers;

import com.microservice.tracingMicroservice.DTOs.ClientLoanFinalApprovedForm;
import com.microservice.tracingMicroservice.DTOs.ClientLoanPreApprovedForm;
import com.microservice.tracingMicroservice.DTOs.ClientLoanRejectForm;
import com.microservice.tracingMicroservice.Entities.TracingEntity;
import com.microservice.tracingMicroservice.Services.TracingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tracing")
public class TracingController {
    @Autowired
    private TracingService tracingService;

    @PostMapping
    public Long addTracing(@RequestBody TracingEntity tracingEntity) { return this.tracingService.addTracing(tracingEntity);}

    @GetMapping("/{id}")
    public TracingEntity getTracingById(@PathVariable Long id) {return this.tracingService.getTracingById(id);}

    @PutMapping("/preApproved")
    public ResponseEntity<Object> updateClientLoanPreApproved(@RequestBody ClientLoanPreApprovedForm form) {
        return this.tracingService.updateClientLoanPreApproved(form);
    }

    @PutMapping("/final")
    public ResponseEntity<Object> updateClientLoanFinal(@RequestBody ClientLoanFinalApprovedForm form) {
        return this.tracingService.updateFinalApproved(form);
    }

    @PutMapping("/reject")
    public ResponseEntity<Object> updateReject(@RequestBody ClientLoanRejectForm form) {
        return this.tracingService.updateReject(form);
    }
}
