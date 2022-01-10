package ru.netology.servlet;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {


  private PostRepository repository;
  private PostService service;
  private PostController controller;

  @Override
  public void init() {
    var context = new AnnotationConfigApplicationContext("ru.netology");
    repository = context.getBean(PostRepository.class);
    service = context.getBean(PostService.class);
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {

    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals("GET") && path.equals("/api/posts")) {
        controller.all(resp);
        return;
      }
      if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
        long id = Long.parseLong(path.replaceAll("[^0-9]", ""));
        controller.getById(id, resp);
        return;
      }
      if (method.equals("POST") && path.equals("/api/posts")) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
        long id = Long.parseLong(path.replaceAll("[^0-9]", ""));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (NotFoundException e){
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

