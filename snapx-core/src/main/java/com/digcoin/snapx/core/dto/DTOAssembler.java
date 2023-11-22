package com.digcoin.snapx.core.dto;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/23 12:59
 * @description
 */
public interface DTOAssembler<D, E> {

    D toDTO(E entity);

    List<D> toDTO(List<E> entities);

    E toEntity(D dto);

}
