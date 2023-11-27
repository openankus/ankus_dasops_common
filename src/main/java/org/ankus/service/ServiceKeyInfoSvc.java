package org.ankus.service;

import org.ankus.model.ServiceKeyInfo;
import org.ankus.model.User;
import org.ankus.repository.ServiceKeyInfoRepository;
import org.ankus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceKeyInfoSvc {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceKeyInfoRepository serviceKeyInfoRepository;


    /**
     * 문자열을 'SHA-256'으로 암호화하여 반환
     * 
     * @param text 암호화할 문자열
     * @return
     */
    private String encrypt(String text) {
        String encryptedText = null;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());

            StringBuilder builder = new StringBuilder();
            for (byte b : md.digest()) {
                builder.append(String.format("%02x", b));
            }

            encryptedText = builder.toString();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

        return encryptedText;
    }

    /**
     * 서비스 키 정보 생성
     *
     * @param loginId 사용자 로그인 ID
     * @return
     */
    public ServiceKeyInfo generateServiceKeyInfo(String loginId){
        User user = userRepository.findUserByLoginId(loginId);

        //  기존에 생성한 서비스키 정보 목록을 찾아, 삭제 처리하기
        List<ServiceKeyInfo> serviceKeyInfoList = serviceKeyInfoRepository.findByUserAndDeleted(user, false);
        for (ServiceKeyInfo serviceKeyInfo : serviceKeyInfoList){
            serviceKeyInfo.setDeleted(true);
        }

        // 신규 서비스키 생성
        String plainText = "ankus" + "@" + loginId + "@" + LocalDateTime.now();
        String servicekey = this.encrypt(plainText);

        //  새로운 서비스키 정보를 생성
        ServiceKeyInfo serviceKeyInfo = new ServiceKeyInfo();
        serviceKeyInfo.setUser(user);
        serviceKeyInfo.setServiceKey(servicekey);
        serviceKeyInfo.setDeleted(false);
        serviceKeyInfoRepository.save(serviceKeyInfo);

        return serviceKeyInfo;
    }

    /**
     * 서비스 키 정보 반환
     *
     * @param loginId 사용자 로그인 ID
     * @return
     */
    public ServiceKeyInfo getServiceKeyInfo(String loginId){
        ServiceKeyInfo serviceKeyInfo = null;

        User user = userRepository.findUserByLoginId(loginId);
        List<ServiceKeyInfo> serviceKeyInfoList = serviceKeyInfoRepository.findByUserAndDeleted(user, false);

        if (serviceKeyInfoList.size() > 0){
            serviceKeyInfo =serviceKeyInfoList.get(0);
        }

        return serviceKeyInfo;
    }

    /**
     * 서비스키의 유효여부를 반환
     * 
     * @param serviceKey 유효성을 검증할 서비스키
     * @return
     */
    public boolean isValidServiceKey(String serviceKey){
        boolean isValid = false;
        
        ServiceKeyInfo serviceKeyInfo = serviceKeyInfoRepository.findByServiceKeyAndDeleted(serviceKey, false);
        if (serviceKeyInfo != null){
            isValid = true;
        }
        
        return isValid;
    }

}
