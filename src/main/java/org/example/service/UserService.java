package org.example.service;

import org.example.dto.CardDTO;
import org.example.dto.ProfileDTO;
import org.example.repository.ProfileRepository;
import org.example.utils.ScannerUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserService {

    ScannerUtils scanner = new ScannerUtils();
    ProfileRepository profileRepository=new ProfileRepository();



    public ProfileDTO login(ProfileDTO profileDTO) {
        ProfileDTO profile = profileRepository.login(profileDTO);


        return profile;

    }

    public boolean registration(ProfileDTO profile) {
        boolean result = profileRepository.registration(profile);
        return result;
    }

    public List<ProfileDTO> profilee(ProfileDTO profileDTO) {
        List<ProfileDTO> profileDTOList = profileRepository.getprfile_list();
        return profileDTOList;
    }


    public List<ProfileDTO> getProfillist() {
        List<ProfileDTO> cardList =profileRepository.getprfile_list();
        return cardList;
    }
}
