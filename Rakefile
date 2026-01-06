require "erb"

VERSION_FILE = "VERSION"
README_TEMPLATE = "docs/README.md.erb"
README_OUTPUT = "README.md"

def current_version
  File.read(VERSION_FILE).strip
end

desc "Generate README.md from ERB template"
task :doc do
  version = current_version
  template = ERB.new(File.read(README_TEMPLATE), trim_mode: "-")
  result = template.result_with_hash(version: version)

  File.write(README_OUTPUT, result)
  puts "Generated #{README_OUTPUT} with version #{version}"
end

desc "Run Clojure tests"
task :test do
  sh "clojure -M:test"
end

desc "Release if VERSION has changed"
task :release do
  changed = `git diff --name-only HEAD #{VERSION_FILE}`.strip

  if changed.empty?
    puts "VERSION has not changed. Nothing to release."
    next
  end

  version = current_version

  puts "Releasing version #{version}..."

  system("clojure -T:build release") or abort("Release failed")

  puts "Release completed."

  Rake::Task[:doc].invoke
end

