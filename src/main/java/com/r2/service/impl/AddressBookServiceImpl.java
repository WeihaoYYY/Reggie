package com.r2.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.entity.AddressBook;
import com.r2.mapper.AddressBookMapper;
import com.r2.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
