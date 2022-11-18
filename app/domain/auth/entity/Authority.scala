package domain.auth.entity

import domain.auth.value_obj.{Permission, Role}

/** 权限实体
 *  @param role
 *    角色
 *  @param permissions
 *    权限资源集合
 */
case class Authority(id: Long, role: Role, permissions: Seq[Permission] = Nil)
