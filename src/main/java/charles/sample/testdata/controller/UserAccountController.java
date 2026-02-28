package charles.sample.testdata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserAccountController {

  @GetMapping("/my-account")
  public String myAccount(Model model) {
    model.addAttribute("nickname", "Charles");
    model.addAttribute("email", "test@test.com");

    return "my-account";
  }
}
