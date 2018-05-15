package apps.proman.service.user.entity;

import static apps.proman.service.common.entity.Entity.SCHEMA;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import apps.proman.service.common.entity.Identifier;
import apps.proman.service.common.entity.ImmutableEntity;

@Entity
@Table(name = "ROLE_PERMISSIONS", schema = SCHEMA)
public class RolePermissionEntity extends ImmutableEntity implements Identifier<Integer>, Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "PERMISSION_ID")
    private PermissionEntity permissionEntity;

    @Override
    public Integer getId() {
        return id;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public PermissionEntity getPermissionEntity() {
        return permissionEntity;
    }

    public void setPermissionEntity(PermissionEntity permissionEntity) {
        this.permissionEntity = permissionEntity;
    }

}