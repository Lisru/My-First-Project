package com.example.backend.service;

import com.example.backend.entity.vo.response.WeatherVO;
import org.springframework.stereotype.Service;

public interface WeatherService {
    WeatherVO fetchWeather(Double longitude,Double latitude);
}
