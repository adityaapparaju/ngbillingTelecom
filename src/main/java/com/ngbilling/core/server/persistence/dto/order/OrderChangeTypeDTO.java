package com.ngbilling.core.server.persistence.dto.order;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ngbilling.core.server.persistence.dto.item.ItemTypeDTO;
import com.ngbilling.core.server.persistence.dto.user.CompanyDTO;


@Entity
@TableGenerator(
        name="order_change_type_GEN",
        table="jbilling_seqs",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue="order_change_type",
        allocationSize = 100
)
@Table(name="order_change_type")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class OrderChangeTypeDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String name;
    private CompanyDTO entity;

    private boolean defaultType = true;
    private Set<ItemTypeDTO> itemTypes = new HashSet<ItemTypeDTO>(0);

    private boolean allowOrderStatusChange = false;

    private int optLock;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator="order_change_type_GEN")
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = true)
    public CompanyDTO getEntity() {
        return entity;
    }

    public void setEntity(CompanyDTO entity) {
        this.entity = entity;
    }

    @Column(name = "default_type", nullable = false, updatable = true)
    public boolean isDefaultType() {
        return defaultType;
    }

    public void setDefaultType(boolean defaultType) {
        this.defaultType = defaultType;
    }

    @Column(name = "allow_order_status_change", nullable = false, updatable = true)
    public boolean isAllowOrderStatusChange() {
        return allowOrderStatusChange;
    }

    public void setAllowOrderStatusChange(boolean allowOrderStatusChange) {
        this.allowOrderStatusChange = allowOrderStatusChange;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="order_change_type_item_type_map",
            joinColumns={@JoinColumn(name="order_change_type_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="item_type_id", referencedColumnName="id", unique = true)})
    public Set<ItemTypeDTO> getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(Set<ItemTypeDTO> itemTypes) {
        this.itemTypes = itemTypes;
    }

    @Version
    @Column(name = "optlock")
    public int getOptLock() {
        return optLock;
    }

    public void setOptLock(int optLock) {
        this.optLock = optLock;
    }
}
