package com.cybr406.goodsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class AccountController {

  private AccountRepository accountRepository;

  @Autowired
  public AccountController(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @GetMapping("/")
  public String index(Model model) {
    List<Account> accounts = accountRepository.findAll();
    model.addAttribute("accounts", accounts);
    return "/index";
  }

  @GetMapping("/account/transfer")
  public String getTransfer() {
    return "/transfer";
  }

  @GetMapping("/account")
  public String account(Model model, Authentication auth) {
    UserDetails user = (UserDetails) auth.getPrincipal();
    Account account = accountRepository.findByUsername(user.getUsername())
            .orElseThrow(NoSuchElementException::new);
    model.addAttribute(account);
    return "/account";
  }
  
  @PostMapping("/account/transfer")
  public String postTransfer(@ModelAttribute Transfer transfer, Model model, Authentication auth) {
    UserDetails user = (UserDetails) auth.getPrincipal();

    Account from = accountRepository.findByUsername(user.getUsername())
            .orElseThrow(NoSuchElementException::new);

    Account to = accountRepository.findById(transfer.getAccount())
            .orElseThrow(NoSuchElementException::new);

    from.setBalance(from.getBalance() - transfer.getAmount());
    to.setBalance(to.getBalance() + transfer.getAmount());

    accountRepository.save(from);
    accountRepository.save(to);

    String message = String.format("Transferred %d to account %d.", transfer.getAmount(), transfer.getAccount());
    System.out.println(message);
    model.addAttribute("status", message);
    return "/transfer";
  }
  
}
