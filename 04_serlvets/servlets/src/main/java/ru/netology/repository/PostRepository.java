package ru.netology.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;


public class PostRepository {

  private Map<Long,Post> posts = new HashMap<>();
  long id;

  public PostRepository(){
    posts.put(1L,new Post(1,"First Post"));
    posts.put(2L,new Post(2,"Second Post"));
    this.id = posts.size();
  }

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));

  }

  public synchronized Post save(Post post) {
    if (post.getId() == 0 && !posts.containsKey(post.getId())) {
      //long id = posts.size();
      Post newPost = new Post(++id, post.getContent());
      posts.put(id, newPost);
    } else if (post.getId() != 0 && posts.containsKey(post.getId())) {
      posts.put(post.getId(), post);
    } else {
      throw new NotFoundException("no such posts found");
    }
    return post;
  }

  public synchronized void removeById(long id) {
    posts.remove(id);
  }
}
