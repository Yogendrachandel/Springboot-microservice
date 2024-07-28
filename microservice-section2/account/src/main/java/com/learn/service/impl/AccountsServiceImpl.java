package com.learn.service.impl;

import com.learn.constants.AccountsConstants;
import com.learn.dto.AccountsDto;
import com.learn.dto.CustomerDto;
import com.learn.entity.Accounts;
import com.learn.entity.Customer;
import com.learn.exception.CustomerAlreadyExistsException;
import com.learn.exception.ResourceNotFoundException;
import com.learn.mapper.AccountsMapper;
import com.learn.mapper.CustomerMapper;
import com.learn.repository.AccountsRepository;
import com.learn.repository.CustomerRepository;
import com.learn.service.IAccountsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class AccountsServiceImpl implements IAccountsService {


    private CustomerRepository customerRepository;
    private AccountsRepository accountsRepository;



    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        log.info("Customer details goiing to fetchh with mobileNumber : {}",mobileNumber);

        Customer customer= customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer","mobileNumber",mobileNumber)
        );

        //fetch account detail

        Accounts accounts =accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Accounts","customerId",customer.getCustomerId().toString())

        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());
        customerDto.setAccountsDto(accountsDto);

        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {

            Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
            );

            /*Note - here we are  creating deleteByCustomerId method  in accountsRepository Interface,because jpa provide default
              support to delete the record based on ID  like findById() ,but here CustomerId is not Primary in Accounts table .
             */
           // Deleting -account record
            accountsRepository.deleteByCustomerId(customer.getCustomerId());

            /*Note - here we are not creating deleteById method in customerRepository Interface,because jpa provide default
              support to delete the record based on ID  like findById() and in customer table customerId is the primary Key.
             */
          // Deleting -customer record
            customerRepository.deleteById(customer.getCustomerId());
            return true;
    }

    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {

        //do mobile validation for new customer already exist or not.
       customerRepository.findByMobileNumber(customerDto.getMobileNumber())
           .ifPresent( customer ->{
             throw  new CustomerAlreadyExistsException("Customer already exist with this mobile number:"+customerDto.getMobileNumber());

           });

       Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
       customer.setCreatedAt(LocalDateTime.now());
       customer.setCreatedBy("Anonymous");

       Customer createdCustomer=  customerRepository.save(customer);
       Accounts accounts =intailizedAccountForNewCustomer(createdCustomer);
       accountsRepository.save(accounts);
    }






    private Accounts intailizedAccountForNewCustomer(Customer createdCustomer) {
        Accounts accounts = new Accounts();
        accounts.setCustomerId(createdCustomer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        accounts.setAccountNumber(randomAccNumber);
        accounts.setAccountType(AccountsConstants.SAVINGS);
        accounts.setBranchAddress(AccountsConstants.ADDRESS);
        accounts.setCreatedAt(LocalDateTime.now());
        accounts.setCreatedBy("SBI");
        return accounts;

    }
}