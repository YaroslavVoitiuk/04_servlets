package ru.netology.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;


public class PostRepository {

  private Map<Long,Post> posts = new ConcurrentHashMap<>();
  AtomicLong id = new AtomicLong();

  public PostRepository(){
    posts.put(1L,new Post(1,"First Post"));
    posts.put(2L,new Post(2,"Second Post"));
   // id.compareAndSet(id, posts.size());
    id.set(posts.size());
    //this.id = posts.size();
  }

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));

  }

  public Post save(Post post) {
    if (post.getId() == 0 && !posts.containsKey(post.getId())) {
      //long id = posts.size();
      Post newPost = new Post(id.incrementAndGet(), post.getContent());
      posts.put(id.get(), newPost);
    } else if (post.getId() != 0 && posts.containsKey(post.getId())) {
      posts.put(post.getId(), post);
    } else {
      throw new NotFoundException("no such posts found");
    }
    return post;
  }

  public void removeById(long id) {
    posts.remove(id);
  }
}
