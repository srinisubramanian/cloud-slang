#   (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
#   All rights reserved. This program and the accompanying materials
#   are made available under the terms of the Apache License v2.0 which accompany this distribution.
#
#   The Apache License is available at
#   http://www.apache.org/licenses/LICENSE-2.0
#
####################################################
#
#   this flow builds the slang-cli
#
#    Inputs:
#      - target_dir - the directory to create the artifacts in
####################################################
namespace: build.build_content

imports:
  build_content: build.build_content
  cmd: io.cloudslang.slang.base.cmd
  files: io.cloudslang.slang.base.files

flow:
  inputs:
    - target_dir: "'target'"
    - target_cli:
        default: 'target_dir + "/slang-cli/slang"'
        overridable: false
    - slang_content_repo:
        default: "'https://github.com/CloudSlang/cloud-slang-content.git'"
        overridable: false
  name: build_cli_flow
  workflow:
    - create_target_dir:
        do:
          files.create_folder:
            - folder_name: target_dir

    - get_slang_content:
        do:
          build_content.get_slang_content:
            - url: slang_content_repo
            - target_dir:
                default:  target_dir + "/slang_content"
                overridable: false

    - copy_verifier:
            do:
              cmd.run_command:
                - command: >
                    "cp ../score-lang-content-verifier/target/*-jar-with-dependencies.jar " +
                    target_dir + "/slang-content-verifer.jar"

    - run_verifier:
        do:
          cmd.run_command:
            - command: >
                "java -jar " +
                target_dir + "/slang-content-verifer.jar " +
                target_dir + "/slang_content/content/"

    - copy_slang_cli:
        do:
          files.copy:
            - source: "'../score-lang-cli/target/slang/'"
            - destination: target_cli

    - copy_content_to_slang_cli:
        do:
          files.copy:
            - source: target_dir + '/slang_content/content'
            - destination: target_cli + '/content'

    - copy_python_lib_to_slang_cli:
        do:
          files.copy:
            - source: target_dir + '/slang_content/python-lib'
            - destination: target_cli + '/python-lib'

#    - precompile_jython_standalone

    - pip_install:
        do:
          cmd.run_command:
            - command: >
                "pip install -t " + target_cli + "/python-lib " +
                "-r " + target_cli + "/python-lib/requirements.txt --compile"

    - chmod_slang_exec:
        do:
          cmd.run_command:
            - command: >
                "chmod +x " + target_cli + "/bin/slang"

#    - add_docs

    - create_zip:
        do:
          files.zip_folder:
            - archive_name: "'slang-cli'"
            - folder_path: 'target_dir + "/slang-cli"'
            - output_folder: target_dir


#    - create_tar_gz