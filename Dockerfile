FROM ghcr.io/graalvm/graalvm-ce:22.3.1 AS builder
WORKDIR /app/lingxi_scala
RUN gu install native-image
RUN curl https://bintray.com/sbt/rpm/rpm > bintray-sbt-rpm.repo \
	&& mv bintray-sbt-rpm.repo /etc/yum.repos.d/ \
	&& yum install -y sbt
COPY . /app/lingxi_scala
WORKDIR /app/lingxi_scala
RUN sbt "graalvm-native-image:packageBin"

FROM oraclelinux:7-slim
COPY --from=builder /app/lingxi_scala/target/graalvm-native-image/lingxi_scala-1.0.0-SNAPSHOT ./app/
CMD ./app/lingxi_scala/lingxi_scala-1.0.0-SNAPSHOT