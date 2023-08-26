package com.driver.services.impl;

import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        if(!isValidCountryName(countryName)){
            throw new Exception("Country not found");
        }

        //given to create new country for country name
        Country country=new Country();
        CountryName countryName1=getCountryName(countryName);
        country.setCountryName(countryName1);
        country.setCode(countryName1.toCode());

        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);
        user.setOriginalCountry(country);

        country.setUser(user);
        //need to save first to get id for user
        userRepository3.save(user);

        String originalIP=country.getCode()+"."+user.getId();
        user.setOriginalIp(originalIP);

        userRepository3.save(user);

        return user;
    }


    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        User user=userRepository3.findById(userId).get();

        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();

        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        serviceProviderRepository3.save(serviceProvider);

        return user;
    }
    private boolean isValidCountryName(String countryName){
        //to be filled later
        if (countryName.equalsIgnoreCase("IND")) {
            return true;
        } else if (countryName.equalsIgnoreCase("USA")) {
            return true;
        } else if (countryName.equalsIgnoreCase("AUS")) {
            return true;
        } else if (countryName.equalsIgnoreCase("CHI")) {
            return true;
        } else if (countryName.equalsIgnoreCase("JPN")) {
            return true;
        } else {
            return false;
        }
    }
    private CountryName getCountryName(String countryName) throws Exception {
        if(countryName.equalsIgnoreCase("IND") ){
            return CountryName.IND;
        }
        else if(countryName.equalsIgnoreCase("UDA") ){
            return CountryName.USA;
        }
        else if(countryName.equalsIgnoreCase("AUS")){
            return CountryName.AUS;
        }
        else if(countryName.equalsIgnoreCase("CHI")){
            return CountryName.CHI;
        }
        else if (countryName.equalsIgnoreCase("JPN")){
            return CountryName.JPN;
        }
        else{
            throw new Exception("Country not found");
        }
}
