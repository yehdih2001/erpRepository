package io.erp.application.cucumber.stepdefs;

import io.erp.application.ErpApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ErpApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
