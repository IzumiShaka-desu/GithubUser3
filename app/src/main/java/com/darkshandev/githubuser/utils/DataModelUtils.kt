package com.darkshandev.githubuser.utils

import com.darkshandev.githubuser.data.models.UserDetail
import com.darkshandev.githubuser.data.models.UserEntity
import com.darkshandev.githubuser.data.models.UserSearch

fun UserDetail.toEntity(): UserEntity = UserEntity(
    this.id,
    this.avatar_url,
    this.bio,
    this.blog,
    this.company,
    this.created_at,
    this.email,
    this.events_url,
    this.followers,
    this.followers_url,
    this.following,
    this.following_url,
    this.gists_url,
    this.gravatar_id,
    this.hireable,
    this.html_url,
    this.location,
    this.login,
    this.name,
    this.node_id,
    this.organizations_url,
    this.public_gists,
    this.public_repos,
    this.received_events_url,
    this.repos_url,
    this.site_admin,
    this.starred_url,
    this.subscriptions_url,
    this.twitter_username,
    this.type,
    this.updated_at,
    this.url
)

fun UserEntity.toModel(): UserDetail = UserDetail(
    this.avatar_url,
    this.bio,
    this.blog,
    this.company,
    this.created_at,
    this.email,
    this.events_url,
    this.followers,
    this.followers_url,
    this.following,
    this.following_url,
    this.gists_url,
    this.gravatar_id,
    this.hireable,
    this.html_url,
    this.id,
    this.location,
    this.login,
    this.name,
    this.node_id,
    this.organizations_url,
    this.public_gists,
    this.public_repos,
    this.received_events_url,
    this.repos_url,
    this.site_admin,
    this.starred_url,
    this.subscriptions_url,
    this.twitter_username,
    this.type,
    this.updated_at,
    this.url
)

fun UserDetail.toUserSearch(): UserSearch = UserSearch(
    this.avatar_url,
    this.events_url,
    this.followers_url,
    this.following_url,
    this.gists_url,
    this.gravatar_id,
    this.html_url,
    this.id,
    this.login,
    this.node_id,
    this.organizations_url,
    this.received_events_url,
    this.repos_url,
    this.site_admin,
    this.starred_url,
    this.subscriptions_url,
    this.type,
    this.url
)

fun UserEntity.toUserSearch(): UserSearch = UserSearch(
    this.avatar_url,
    this.events_url,
    this.followers_url,
    this.following_url,
    this.gists_url,
    this.gravatar_id,
    this.html_url,
    this.id,
    this.login,
    this.node_id,
    this.organizations_url,
    this.received_events_url,
    this.repos_url,
    this.site_admin,
    this.starred_url,
    this.subscriptions_url,
    this.type,
    this.url
)

fun List<UserEntity>.toListModelSearch(): List<UserSearch> = this.map { it.toUserSearch() }