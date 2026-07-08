package com.sarthak.agenticai.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "wishlist",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "user_id",
                                "product_id"
                        }
                )
        }
)
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many wishlist items belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many wishlist items belong to one product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Wishlist() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}