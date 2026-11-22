# DevStudy API

## Overview

A small Spring Boot API created for the INF 345 Fundamentals of DevOps course.

## Requirements

- Java 17

## Run

Run the application:

```bash
./scripts/run.sh
```

By default, the application runs on port `8080`.

To use another port:

```bash
PORT=9000 ./scripts/run.sh
```

## Test

Run automated tests:

```bash
./scripts/test.sh
```

Expected output at the end:

```text
TESTS: 4/4
```

## Endpoints

### GET /

Returns:

```text
DevStudy API
```

### GET /healthz

Health check endpoint.

Returns:

```text
ok
```

### GET /topics

Returns:

```json
["Git","Linux","Docker"]
```

## GitOps Deployment

GitHub Actions runs the tests, builds and scans the image, then publishes it to
`ghcr.io/aibek-koken/devstudy-api` with the full SHA of a commit on `main`.
The image tag in `k8s/deployment.yaml` is the version to deploy. The Argo CD
Application in `argocd/application.yaml` watches `main` and syncs `k8s/` into
the `m5` namespace. It automatically restores manual changes (`selfHeal`) and
removes objects deleted from Git (`prune`). CI uses only temporary clusters for
deployment tests; it does not deploy to the application cluster.

The Deployment requires a Secret named `app-secret` with an `API_KEY` entry.
Create it in the target cluster before starting the Application. Generate a
fresh value there; never add the Secret or its value to Git:

```bash
kubectl create namespace m5 --dry-run=client -o yaml | kubectl apply -f -
kubectl -n m5 create secret generic app-secret \
  --from-literal="API_KEY=$(openssl rand -hex 16)"
kubectl -n m5 get secret app-secret -o name
```

Install Argo CD in its `argocd` namespace using the pinned course instructions
for Week 10, then register the Application once:

```bash
kubectl apply -f argocd/application.yaml
kubectl -n argocd get application devstudy-api
kubectl -n m5 rollout status deployment/devstudy-api --timeout=300s
kubectl -n m5 get deployment devstudy-api -o jsonpath='{..image}'
```

The Application must show `Synced` and `Healthy`. To deploy a new image, first
merge a change to `main` and wait for its CI publish job to succeed. Copy that
merge commit's full SHA into `k8s/deployment.yaml` as the image tag, then merge
the image change through another pull request. Argo CD detects the Git change
and rolls out the published image. An unpublished SHA cannot be deployed.

To check M5, run the official Week 10 `mark_m5.py --path .` for static checks.
The `M5 check` GitHub Actions workflow runs the same marker with `--kind` on a
temporary cluster and verifies sync, self-healing, and prune. It does not
touch the `m4-local` namespace or any persistent cluster.

## Rollback

Argo CD deploys the image declared on `main` in `k8s/deployment.yaml`. If an
image change is bad, undo its Git commit and merge the revert through a pull
request. For a direct commit or a squash merge:

```bash
git log --oneline -- k8s/deployment.yaml
git revert <image-change-commit>
git push
```

For a regular merge commit, use `git revert -m 1 <merge-commit>` instead. After
the revert reaches `main`, Argo CD syncs the previous image automatically.
Verify that the Application is `Synced` and `Healthy`, and that the Deployment
image has the previous SHA. `kubectl rollout undo` is not a GitOps rollback:
Argo CD would restore the image still declared in Git.
