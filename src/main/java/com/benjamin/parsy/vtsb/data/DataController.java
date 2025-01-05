package com.benjamin.parsy.vtsb.data;

import com.benjamin.parsy.vtsb.data.dto.DetailResponseDto;
import com.benjamin.parsy.vtsb.data.dto.EndpointResponseDto;
import com.benjamin.parsy.vtsb.data.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
public class DataController {

    private final ThreadSafeDataCollector dataCollector;

    public DataController(ThreadSafeDataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @GetMapping("/endpoints")
    public ResponseEntity<EndpointResponseDto> getDataEndpoints() {

        Set<EndpointResponseDto.Endpoint> endpointSet = dataCollector.getTotalCallsByEndpoint().entrySet()
                .stream()
                .map(e -> new EndpointResponseDto.Endpoint(e.getKey(), e.getValue()))
                .collect(Collectors.toSet());

        EndpointResponseDto endpointResponseDto = new EndpointResponseDto(dataCollector.getTotalCalls(), endpointSet);

        return ResponseEntity.ok(endpointResponseDto);
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponseDto> getDataUsers() {

        UserResponseDto userResponseDto = new UserResponseDto(dataCollector.getTotalUsers(),
                dataCollector.getUserList());

        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/details")
    public ResponseEntity<DetailResponseDto> getDataDetails() {

        Set<DetailResponseDto.Detail> detailSet = dataCollector.getCountRequestByEndpointByUser().entrySet().stream()
                .map(entry -> {

                    Set<DetailResponseDto.Detail.Endpoint> endpointSet = entry.getValue().entrySet().stream()
                            .map(subEntry -> new DetailResponseDto.Detail.Endpoint(subEntry.getKey(), subEntry.getValue().get()))
                            .collect(Collectors.toSet());

                    return new DetailResponseDto.Detail(entry.getKey(), endpointSet);
                })
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new DetailResponseDto(dataCollector.getTotalCalls(), detailSet));
    }

}
