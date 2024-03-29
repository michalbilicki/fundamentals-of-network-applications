package com.lapciakbilicki.pas2.service;

import com.lapciakbilicki.pas2.model.Role.Role;
import com.lapciakbilicki.pas2.model.account.Account;
import com.lapciakbilicki.pas2.repository.AccountRepository;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
public class AccountService extends ServiceAdapter<Account> implements Serializable {

    @Inject
    RoleService roleService;

    @Inject
    private AccountRepository accountRepository;

    public AccountService() {
    }

    @PostConstruct
    public void init() {
        this.repository = accountRepository;
    }

    public boolean checkLoginUnique(String login) {
        List<Account> accounts = repository.getByCondition(
                account -> account.getLogin().equals(login)
        );
        return accounts.isEmpty();
    }

    @Override
    public boolean add(Account item) {
        if (checkLoginUnique(item.getLogin())) {
            return repository.add(item);
        } else {
            return false;
        }
    }

    public void updateAccount(Account item) {
        this.repository.update(item);
    }

    public Account getAccountByLoginAndPassword(String login, String password) {
        return this.repository.getAll()
                .stream()
                .filter(account -> account.getLogin().equals(login))
                .filter(account -> account.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<Account> filterAccount(String input) {
        return this.repository.getAll()
                .stream()
                .filter(acc -> acc.getLogin().contains(input))
                .collect(Collectors.toList());
    }

    public void changeAccountActivity(String id) {
        Account accountToChange = this.get(id);
        if (accountToChange != null) {
            accountToChange.setActive(!accountToChange.isActive());
        }
    }

    public boolean createUserWithRoles(String[] roles, String login, String password, String fullName) {
        List<Role> usersRoles = new ArrayList<>();
        for (String role : roles) {
            usersRoles.add(roleService.get(role));
        }
        return add(new Account(UUID.randomUUID().toString(), login, password, fullName, true, usersRoles));
    }

    public void updateAccountWithRole(String id, String login, String password, String fullName, boolean active, String[] roles) {
        List<Role> usersRoles = new ArrayList<>();
        for (String role : roles) {
            usersRoles.add(roleService.get(role));
        }

        Account account = new Account(id, login, password, fullName, active, usersRoles);

        this.update(account);
    }

    public boolean createClientUser(String login, String password, String fullName) {
        return add(new Account(UUID.randomUUID().toString(), login, password, fullName, false, roleService.getByCondition(role -> role.getName().equals("Client"))));
    }

    public Account getAccountByLogin(String login) {
        return this.repository
                .getAll()
                .stream()
                .filter(account -> account.getLogin()
                .equals(login))
                .findAny()
                .orElse(null);
    }

}
