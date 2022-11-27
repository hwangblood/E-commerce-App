package com.androidfactory.fakestore.home.profile

import androidx.annotation.DrawableRes
import com.airbnb.epoxy.TypedEpoxyController
import com.androidfactory.fakestore.R
import com.androidfactory.fakestore.databinding.EpoxyModelProfileSignedInItemBinding
import com.androidfactory.fakestore.databinding.EpoxyModelProfileSignedOutBinding
import com.androidfactory.fakestore.epoxy.ViewBindingKotlinModel
import com.androidfactory.fakestore.extensions.toPx
import com.androidfactory.fakestore.home.cart.epoxy.DividerEpoxyModel
import com.androidfactory.fakestore.model.domain.user.User

class ProfileEpoxyController(
    private val userProfileItemGenerator: UserProfileItemGenerator,
    private val profileUiActions: ProfileUiActions
) : TypedEpoxyController<User?>() {

    override fun buildModels(data: User?) {
        if (data == null) {
            SignedOutEpoxyModel(onSignIn = { username, password ->
                profileUiActions.onSignIn(username, password)
            }).id("signed_out_state").addTo(this)
        } else {
            userProfileItemGenerator.buildItems(user = data).forEach { profileItem ->
                SignedInItemEpoxyModel(
                    iconRes = profileItem.iconRes,
                    headerText = profileItem.headerText,
                    infoText = profileItem.infoText,
                    onClick = { profileUiActions.onProfileItemSelected(profileItem.iconRes) }
                ).id(profileItem.iconRes).addTo(this)

                DividerEpoxyModel(
                    horizontalMargin = 20.toPx()
                ).id("divider_${profileItem.iconRes}").addTo(this)
            }

            SignedInItemEpoxyModel(
                iconRes = R.drawable.ic_round_logout_24,
                headerText = "Logout",
                infoText = "Sign out of your account",
                onClick = { profileUiActions.onProfileItemSelected(R.drawable.ic_round_logout_24) }
            ).id(R.drawable.ic_round_logout_24).addTo(this)
        }
    }

    data class SignedOutEpoxyModel(
        val onSignIn: (String, String) -> Unit
    ) : ViewBindingKotlinModel<EpoxyModelProfileSignedOutBinding>(R.layout.epoxy_model_profile_signed_out) {

        override fun EpoxyModelProfileSignedOutBinding.bind() {
            button.setOnClickListener {
                onSignIn("donero", "ewedon")
            }
        }
    }

    data class SignedInItemEpoxyModel(
        @DrawableRes val iconRes: Int,
        val headerText: String,
        val infoText: String,
        val onClick: () -> Unit
    ) : ViewBindingKotlinModel<EpoxyModelProfileSignedInItemBinding>(R.layout.epoxy_model_profile_signed_in_item) {

        override fun EpoxyModelProfileSignedInItemBinding.bind() {
            iconImageView.setImageResource(iconRes)
            headerTextView.text = headerText
            infoTextView.text = infoText
            root.setOnClickListener { onClick() }
        }
    }
}