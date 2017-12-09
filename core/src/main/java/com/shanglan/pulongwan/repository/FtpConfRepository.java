package com.shanglan.pulongwan.repository;

import com.shanglan.pulongwan.entity.FTPConf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FtpConfRepository extends JpaRepository<FTPConf,Integer> {
    FTPConf findByName(String name);
}
