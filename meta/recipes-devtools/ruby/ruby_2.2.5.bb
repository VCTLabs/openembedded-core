require ruby.inc

SRC_URI[md5sum] = "bd8e349d4fb2c75d90817649674f94be"
SRC_URI[sha256sum] = "30c4b31697a4ca4ea0c8db8ad30cf45e6690a0f09687e5d483c933c03ca335e3"
#SRC_URI[md5sum] = "326e99ddc75381c7b50c85f7089f3260"
#SRC_URI[sha256sum] = "5ffc0f317e429e6b29d4a98ac521c3ce65481bfd22a8cf845fa02a7b113d9b44"

# it's unknown to configure script, but then passed to extconf.rb
# maybe it's not really needed as we're hardcoding the result with
# 0001-socket-extconf-hardcode-wide-getaddr-info-test-outco.patch
UNKNOWN_CONFIGURE_WHITELIST += "--enable-wide-getaddrinfo"

PACKAGECONFIG ??= ""
PACKAGECONFIG += "${@bb.utils.contains('DISTRO_FEATURES', 'ipv6', 'ipv6', '', d)}"

PACKAGECONFIG[valgrind] = "--with-valgrind=yes, --with-valgrind=no, valgrind"
PACKAGECONFIG[gpm] = "--with-gmp=yes, --with-gmp=no, gmp"
PACKAGECONFIG[ipv6] = ",--enable-wide-getaddrinfo,"

EXTRA_OECONF = "\
    --disable-versioned-paths \
    --disable-rpath \
    --disable-dtrace \
    --enable-shared \
    --enable-load-relative \
"

EXTRA_OECONF_append_class-native = "\
    --disable-install-rdoc \
"

EXTRA_OEMAKE_append_class-target = " \
    LIBRUBYARG='-lruby-static' \
"

EXTRA_OEMAKE_append_class-native = "\
    LIBRUBYARG="-L./ -lruby" \
    EXTLDFLAGS="${BUILD_LDFLAGS}" \
"

do_install() {
    oe_runmake 'DESTDIR=${D}' install
}

PACKAGES =+ "${PN}-ri-docs ${PN}-rdoc"

SUMMARY_${PN}-ri-docs = "ri (Ruby Interactive) documentation for the Ruby standard library"
RDEPENDS_${PN}-ri-docs = "${PN}"
FILES_${PN}-ri-docs += "${datadir}/ri"

SUMMARY_${PN}-rdoc = "RDoc documentation generator from Ruby source"
RDEPENDS_${PN}-rdoc = "${PN}"
FILES_${PN}-rdoc += "${libdir}/ruby/*/rdoc ${bindir}/rdoc"

FILES_${PN} += "${datadir}/rubygems"

BBCLASSEXTEND = "native"
