/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.quarkus.reactive;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * FruitResource
 *
 * @author tabuyos
 * @since 2022/2/16
 */
@Path("/fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitResource {

  @GET
  public Uni<List<Fruit>> get() {
    return Fruit.listAll(Sort.by("name"));
  }

  @GET
  @Path("/{id}")
  public Uni<Fruit> getSingle(Long id) {
    return Fruit.findById(id);
  }

  @POST
  public Uni<Response> create(Fruit fruit) {
    return Panache.<Fruit>withTransaction(fruit::persist)
                  .onItem()
                  .transform(inserted -> Response.created(URI.create("/fruits/" + inserted.id)).build());
  }
}
