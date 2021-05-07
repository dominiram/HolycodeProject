package app.naum.myapplication.networking

import app.naum.myapplication.models.UserModel
import app.naum.myapplication.networking.entities.UserInfoNetworkEntity
import app.naum.myapplication.utils.EntityMapper
import javax.inject.Inject

class SearchUserNetworkMapper
@Inject
constructor() : EntityMapper<UserInfoNetworkEntity, UserModel> {
    override fun mapFromEntity(entity: UserInfoNetworkEntity): UserModel {
        return UserModel(
            entity.login,
            entity.id,
            entity.node_id,
            entity.avatar_url,
            entity.gravatar_id,
            entity.url,
            entity.html_url,
            entity.followers_url,
            entity.following_url,
            entity.gists_url,
            entity.starred_url,
            entity.subscriptions_url,
            entity.organizations_url,
            entity.repos_url,
            entity.events_url,
            entity.received_events_url,
            entity.type,
            entity.site_admin,
            entity.name,
            entity.company,
            entity.blog,
            entity.location,
            entity.email,
            entity.hireable,
            entity.bio,
            entity.twitter_username,
            entity.public_repos,
            entity.public_gists,
            entity.followers,
            entity.following,
            entity.created_at,
            entity.updated_at
        )
    }

    override fun mapToEntity(domainModel: UserModel): UserInfoNetworkEntity {
        TODO("Not yet implemented")
    }
}