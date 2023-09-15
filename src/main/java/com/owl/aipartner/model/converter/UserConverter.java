package com.owl.aipartner.model.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.owl.aipartner.model.dto.UserDTO;
import com.owl.aipartner.model.po.UserPO;

public class UserConverter {
    private UserConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static UserPO getUserPO(UserDTO userDTO) {
        UserPO userPO = new UserPO();
        userPO.setId(userDTO.getId());
        userPO.setName(userDTO.getName());
        userPO.setAge(userDTO.getAge());
        return userPO;
    }

    public static UserDTO getUserDTO(UserPO userPO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userPO.getId());
        userDTO.setName(userPO.getName());
        userDTO.setAge(userPO.getAge());
        return userDTO;
    }

    public static List<UserDTO> getUserDTOList(List<UserPO> data) {
        return data.stream()
                .map(UserConverter::getUserDTO)
                .collect(Collectors.toList());
    }

}
