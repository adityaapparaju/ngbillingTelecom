package com.ngbilling.core.server.persistence.dao.item.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ngbilling.core.server.persistence.dao.AbstractJpaDAO;
import com.ngbilling.core.server.persistence.dao.item.ItemTypeCustomDAO;
import com.ngbilling.core.server.persistence.dao.item.ItemTypeDAO;
import com.ngbilling.core.server.persistence.dto.item.ItemTypeDTO;
import com.ngbilling.core.server.persistence.dto.order.OrderDTO;
import com.ngbilling.core.server.util.ServerConstants;

public class ItemTypeCustomDAOImpl extends AbstractJpaDAO<ItemTypeDTO> implements ItemTypeDAO{

	private static final String FIND_ALL_TYPES_LINKED_THROUGH_PRODUCT = "SELECT distinct t.id FROM ItemTypeDTO t " +
            "JOIN t.items it " +
            "WHERE it.id IN " +
            "( SELECT it2.id FROM ItemDTO it2 " +
            " JOIN it2.itemTypes its2 " +
            " WHERE its2.id = :the_id)";
	@Override
	public List<Integer> findAllTypesLinkedThroughProduct(Integer typeId) {
		// TODO Auto-generated method stub
		
		Query query = getEntityManager().createQuery(FIND_ALL_TYPES_LINKED_THROUGH_PRODUCT
	            );
		
		 query.setParameter(1,  typeId);
		 
		return  query.getResultList();
	}

	private static final String GET_CHILD_ITEM_CATEGORIES = "select u from ItemTypeDTO u where u.parent.id = ?1";
	@Override
	public List<ItemTypeDTO> getChildItemCategories(Integer itemTypeId) {
		// TODO Auto-generated method stub
		TypedQuery<ItemTypeDTO> query = getEntityManager().createQuery(GET_CHILD_ITEM_CATEGORIES,
				ItemTypeDTO.class);
		
		 query.setParameter(1,  itemTypeId);
		 
		return query.getResultList();
	}
	private static final String GET_CREATE_INTERNAL_PLANS_TYPE = "select u from ItemTypeDTO u where u.entity.id = ?1 "
			+ "and u.description = " + ServerConstants.PLANS_INTERNAL_CATEGORY_NAME + " and u.internal = 1";

	@Override
	public ItemTypeDTO getCreateInternalPlansType(Integer entityId, String description) {
		// TODO Auto-generated method stub
		TypedQuery<ItemTypeDTO> query = getEntityManager().createQuery(GET_CREATE_INTERNAL_PLANS_TYPE,
				ItemTypeDTO.class);
		
		 query.setParameter(1,  entityId);
		return query.getSingleResult();
	}

	private static final String FIND_ITEM_TYPE_WITH_ASSET_MANAGEMENT_FOR_ITEM = "select u from ItemTypeDTO u where u.items.id = ?1 "
			+ "and u.allowAssetManagement = 1";
	@Override
	public ItemTypeDTO findItemTypeWithAssetManagementForItem(int itemId) {
		// TODO Auto-generated method stub
		TypedQuery<ItemTypeDTO> query = getEntityManager().createQuery(FIND_ITEM_TYPE_WITH_ASSET_MANAGEMENT_FOR_ITEM,
				ItemTypeDTO.class);
		
		 query.setParameter(1,  itemId);
		return query.getSingleResult();
	}

	private static final String FIND_BY_DESCRIPTION = "SELECT  t FROM ItemTypeDTO t left join "
			+ "t.entities it " +
            "WHERE it.id = ?1 and t.description = ?2)";
	@Override
	public ItemTypeDTO findByDescription(Integer entityId, String description) {
		// TODO Auto-generated method stub
		TypedQuery<ItemTypeDTO> query = getEntityManager().createQuery(FIND_BY_DESCRIPTION,ItemTypeDTO.class
	            );
		
		 query.setParameter(1,  entityId);
		 query.setParameter(2,  description);

		return  query.getSingleResult();
	}

	private static final String FIND_BY_GLOBAL_DESCRIPTION = "SELECT  t FROM ItemTypeDTO t " +
					
                "WHERE t.entity.id = ?1 and t.description = ?2 and t.global = 1)";
	@Override
	public ItemTypeDTO findByGlobalDescription(Integer entityId, String description) {
		// TODO Auto-generated method stub
		TypedQuery<ItemTypeDTO> query = getEntityManager().createQuery(FIND_BY_GLOBAL_DESCRIPTION,ItemTypeDTO.class
	            );
		
		 query.setParameter(1,  entityId);
		return  query.getSingleResult();
	}

	private static final String FIND_BY_ENTITY_ID = "SELECT  t FROM ItemTypeDTO t " +
            "left JOIN t.entities it " +
            "WHERE it.id = ?1)";
	@Override
	public List<ItemTypeDTO> findByEntityId(Integer entityId) {
		// TODO Auto-generated method stub
		TypedQuery<ItemTypeDTO> query= getEntityManager().createQuery(FIND_BY_ENTITY_ID, ItemTypeDTO.class
	            );
		
		 query.setParameter(1,  entityId);
		return  query.getResultList();
	}

	
	private static final String FIND_ITEM_CATEGORIES = "SELECT  t FROM ItemTypeDTO t "
				+ "left join t.entities it " +
				
                "WHERE t.global = 1 or it.entities.id in (:entities) ";
	@Override
	public List<ItemTypeDTO> findItemCategories(Integer entity, List<Integer> entities, boolean isRoot) {
		// TODO Auto-generated method stub
		
		String sql = FIND_ITEM_CATEGORIES;
		
		String sqlFinal = ((isRoot) ? (sql + (" or it.entities.parent.id = :id")) : sql) + " order by t.id asc";
		Query query = getEntityManager().createNamedQuery(sqlFinal);
		
		if(isRoot)
		 query.setParameter("id",  entity);
		 query.setParameter("entities",  entities);

		return  query.getResultList();
	}

	private static final String IS_ASSOCIATED_TO_ACTIVE_ORDER = "select u from OrderDTO u where u.deleted=0 and (u.activeUntil == null "
			+ "or u.activeUntil > ?1) and u.baseUserByUserId.id = ?2 and u.lines.item.nulltypes.id = ?3";
	
	@Override
	public Boolean isAssociatedToActiveOrder(Integer userId, Integer itemTypeId, Date activeSince, Date activeUntil) {
		// TODO Auto-generated method stub
		
		TypedQuery<OrderDTO> query= getEntityManager().createQuery(IS_ASSOCIATED_TO_ACTIVE_ORDER, OrderDTO.class
	            );
		
		 query.setParameter(1,  activeUntil);
		 query.setParameter(2,  userId);
		 query.setParameter(3,  itemTypeId);
		 if (activeUntil != null)
		 query.setParameter(4,  activeUntil);

		return  query.getResultList().size() >0 && query.getResultList() != null;
		
	}

	private static final String GET_BY_ID = "SELECT  t FROM ItemTypeDTO t "
				+ "left join t.entities it " +
				
                "WHERE t.id = :id";
	
	private static final String GET_BY_ID_PARENT = " and (t.entity.id=:entityId or (t.entity.id=:parentEntityId and t.global = 1 ) or (t.entity.id=:parentEntityId"
			+ " and it.id in (:entityId )))" ;
	@Override
	public ItemTypeDTO getById(Integer itemTypeId, Integer entityId, Integer parentEntityId) {
		// TODO Auto-generated method stub
		if(null == itemTypeId || null == entityId) {
			throw new IllegalArgumentException("Arguments itemTypeId and entityId can not be null");
		}
		String sql = GET_BY_ID;
		
		String sqlFinal = (null != parentEntityId) ? sql + GET_BY_ID_PARENT :
		sql + " and t.entity.id=:entityId";
		Query query = getEntityManager().createNamedQuery(sqlFinal);
		
		 query.setParameter("id",  itemTypeId);
		 query.setParameter("entityId",  entityId);
		 if (null != parentEntityId)
		 query.setParameter("parentEntityId",  parentEntityId);

		return (ItemTypeDTO) query.getSingleResult();
	}
	@Override
	public List<ItemTypeDTO> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ItemTypeDTO> findAllById(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <S extends ItemTypeDTO> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public <S extends ItemTypeDTO> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void deleteInBatch(Iterable<ItemTypeDTO> entities) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ItemTypeDTO getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <S extends ItemTypeDTO> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <S extends ItemTypeDTO> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Page<ItemTypeDTO> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <S extends ItemTypeDTO> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional<ItemTypeDTO> findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean existsById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteAll(Iterable<? extends ItemTypeDTO> entities) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public <S extends ItemTypeDTO> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <S extends ItemTypeDTO> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <S extends ItemTypeDTO> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public <S extends ItemTypeDTO> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

}
