#!/usr/bin/env python3
"""
Attribue le custom claim Firebase { "admin": true } à un utilisateur
sans recourir aux Cloud Functions (plan Spark 100% gratuit).

Usage :
  export GOOGLE_APPLICATION_CREDENTIALS=./github-actions-sa.json
  python3 scripts/set_admin_claim.py --email mandupaix@gmail.com
"""

import argparse
import firebase_admin
from firebase_admin import auth, credentials


def main() -> None:
    parser = argparse.ArgumentParser(description="Set Firebase custom claim admin=true")
    parser.add_argument("--email", required=True, help="Email de l'administrateur")
    args = parser.parse_args()

    if not firebase_admin._apps:
        firebase_admin.initialize_app(credentials.ApplicationDefault())

    user = auth.get_user_by_email(args.email)
    existing_claims = user.custom_claims or {}
    existing_claims["admin"] = True
    auth.set_custom_user_claims(user.uid, existing_claims)
    print(f"Custom claim admin=true attribué avec succès à {user.email} (uid={user.uid}).")
    print("L'utilisateur doit se reconnecter dans l'application pour rafraîchir son token.")


if __name__ == "__main__":
    main()
