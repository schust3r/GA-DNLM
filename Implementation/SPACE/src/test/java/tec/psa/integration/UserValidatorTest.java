package tec.psa.integration;

import static org.junit.Assert.assertTrue;

import org.apache.xerces.parsers.SecurityConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import tec.psa.model.User;
import tec.psa.validator.UserValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserValidatorTest {

  @Autowired
  private UserValidator userValidator;
  private User usuario;
  public Errors errors;

  @Before
  public void setUp() {
    usuario = new User();
    usuario.setUsername("username");
    usuario.setPassword("password");
    errors = new BeanPropertyBindingResult(usuario, "usuario");
  }

  @Test
  public void validationTest() {
    userValidator.validate(usuario, errors);
    assertTrue("Hay errores en la validación", errors.hasErrors() == false);
  }

}
