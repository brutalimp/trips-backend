package com.zshuyin.trips.controller;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.zshuyin.trips.bean.Trip;
import com.zshuyin.trips.bean.User;
import com.zshuyin.trips.repository.TripRepository;
import com.zshuyin.trips.service.GridfsService;
import com.zshuyin.trips.utils.Position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class TripController {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    GridfsService gridfsService;

    @PostMapping("/trip")
    public ResponseEntity addTrip(HttpServletRequest request) {

        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
        User user = (User) request.getAttribute("user");
        List<String> images = new ArrayList<String>();
        ;
        for (MultipartFile file : files) {
            String fileID = UUID.randomUUID().toString();
            images.add(fileID);
            gridfsService.save(file, fileID);
        }
        Trip trip = new Trip();
        String address = params.getParameter("address");
        String longitude = params.getParameter("longitude");
        String latitude = params.getParameter("latitude");
        String description = params.getParameter("description");
        String timestamp = String.valueOf(System.currentTimeMillis());
        trip.setImages(images);
        trip.setPosition(new Position(longitude, latitude, address));
        trip.setTimestamp(timestamp);
        trip.setUserId(user.getId());
        trip.setDescription(description);
        tripRepository.save(trip);
        return ResponseEntity.ok().build();
    }

    @GetMapping("trip")
    public ResponseEntity<List<Trip>> getTrips(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String userId = user.getId();
        Optional<List<Trip>> result = tripRepository.findByUserId(userId);
        List<Trip> trips = result.get();
        return ResponseEntity.ok().body(trips);
    }

    public static Map<String, Object> transBean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

}