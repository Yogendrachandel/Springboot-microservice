package com.learn.service.impl;

import com.learn.dto.AccountsDto;
import com.learn.dto.CardsDto;
import com.learn.dto.CustomerDetailsDto;
import com.learn.entity.Accounts;
import com.learn.entity.Customer;
import com.learn.exception.ResourceNotFoundException;
import com.learn.mapper.AccountsMapper;
import com.learn.mapper.CustomerMapper;
import com.learn.repository.AccountsRepository;
import com.learn.repository.CustomerRepository;
import com.learn.service.ICustomersService;
import com.learn.service.client.CardsFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;


    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        System.out.println(" CUSTOMER - "+customer.toString());
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

      

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());

        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));



        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber,correlationId);


        if(null != cardsDtoResponseEntity){

            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }

        return customerDetailsDto;

    }
}
