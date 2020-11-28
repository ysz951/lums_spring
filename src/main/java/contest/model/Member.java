package contest.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Member implements Serializable{
    @Id @GeneratedValue private Long id;

    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    private boolean blocked;

    @NotNull
    private String password;

    private Role role;

    public enum Role {
        SUPERUSER,
        ADMIN,
        CUSTOMER,
        EXPERT
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Sale> sales = new HashSet<Sale>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Log> userLogs = new HashSet<Log>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "admin")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Log> adminLogs = new HashSet<Log>();

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setUserLogs(Set<Log> userLogs) {
        this.userLogs = userLogs;
    }

    public void setAdminLogs(Set<Log> adminLogs) {
        this.adminLogs = adminLogs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Sale> getSales() {
        return sales;
    }

    public Set<Log> getUserLogs() {
        return userLogs;
    }

    public Set<Log> getAdminLogs() {
        return adminLogs;
    }
}
