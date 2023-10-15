package com.example.createAPI.controller;

import com.example.createAPI.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.LongStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);


    @Value("${server.port}")
    private int port;

    @GetMapping("/port")
    public int getPort() {
        return port;
    }

    @GetMapping("/calculate")
    public void getCalc() {
        long startTime = System.currentTimeMillis();
        logger.info("метод запустился");
        Integer reduce = Stream.iterate(1, a -> a + 1)
                .limit(5_000_000)
                .reduce(0, Integer::sum);
        long finishTime = System.currentTimeMillis() - startTime;
        logger.info("время работы = " + finishTime);


        long startTime2 = System.currentTimeMillis();
        logger.info("оптимизированный метод запустился");
        LongStream.rangeClosed(1, 5_000_000)
                .reduce(0L, Long::sum);
        long finishTime2 = System.currentTimeMillis() - startTime2;
        logger.info("время работы после оптимизации= " + finishTime2);

    }

}
