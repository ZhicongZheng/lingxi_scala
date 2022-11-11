package domain.user.entity

import domain.auth.entity.Authority
import domain.user.value_obj.User

/** 账号实体
 *  @param user
 *    用户基本信息
 *  @param authority
 *    权限相关信息
 */
case class Account(user: User, authority: Authority)
