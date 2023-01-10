package com.kodilla.ecommercee.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GROUPS")
public class Group {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "GROUP_ID", unique = true)
    private Long groupId;
    @Column(name = "NAME")
    private String groupName;
    @Column(name = "DESCRIPTION")
    private String groupDescription;
    @OneToMany(
            targetEntity = Product.class,
            mappedBy = "group",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

}