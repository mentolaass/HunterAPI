package ru.mentola.hunterapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.mentola.hunterapi.model.ServiceStatus;

@RestController
@RequestMapping("")
public final class AppController {
    @Value("${app.version}")
    private String version;

    @GetMapping("/status")
    public ResponseEntity<ServiceStatus> status() {
        return ResponseEntity.ok(new ServiceStatus(version));
    }

    @GetMapping("")
    public RedirectView index() {
        return new RedirectView("https://github.com/mentolaass/CounterStrikeHunter");
    }
}