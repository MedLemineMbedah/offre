package com.example.offres.Service;

import java.util.List;

import com.example.offres.Model.Admin;
import com.example.offres.Model.Personne;
import com.example.offres.Repository.AdminRepository;
import com.example.offres.Repository.PersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

//import javax.management.relation.Role;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PersonneRepository persoRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        List<Admin> adminsList = adminRepository.findUser(userName);
        String url = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURL().toString();

        if (adminsList != null && adminsList.size() == 1 && url.equals("http://localhost:8080/admin/login")) {
            Admin users = adminsList.get(0);

            return User.builder()
                    .username(users.getEmail())
                    //change here to store encoded password in db
                    .password(users.getPassword())
                    .roles("ADMIN")
                    .build();
        } else {

            List<Personne> persoList = persoRepository.findPerso(userName);

            if (persoList != null && persoList.size() == 1 && url.equals("http://localhost:8080/user/login")) {
                Personne users = persoList.get(0);


                    return User.builder()
                            .username(users.getEmail())
                            .password(users.getPassword())
                            .roles("USER")
                            .build();
                } else {
                    throw new UsernameNotFoundException("User Name is not Found");
                }

        }
    }
}
