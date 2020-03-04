package com.gunio.demo;

import java.util.*;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
public class BlogpostController {
    private static Logger log = LoggerFactory.getLogger(BlogpostController.class);
    private static Map<String, Blogpost> store;

    static {
        store = Collections.synchronizedMap(new HashMap<String, Blogpost>());
    }


    @RequestMapping(method = RequestMethod.POST, value = "/addAsync")
    public WebAsyncTask<ResponseEntity<?>> addBlogpostAsync(@RequestBody Blogpost b) {
        return new WebAsyncTask<ResponseEntity<?>>(1000, () -> {
            store.put(b.getId(), b);
            return ResponseEntity.ok().build();
        });
    }


    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ResponseEntity<?> addBlogpost(@RequestBody Blogpost b) {
        store.put(b.getId(), b);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/blogpost/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WebAsyncTask<Blogpost> getBlog(@PathVariable("id") String id) {
        log.info(Thread.currentThread().getName());
        Callable<Blogpost> callable = () -> {
            Thread.sleep(100L);
            Blogpost b = store.get(id);
            return b;
        };
        return new WebAsyncTask<Blogpost>(callable);
    }


}