require "open3"

VERSION_FILE = "VERSION"

desc "Release if VERSION has changed"
task :release do
  # 1. VERSION に変更があるか確認
  changed = `git diff --name-only HEAD #{VERSION_FILE}`.strip

  if changed.empty?
    puts "VERSION has not changed. Nothing to release."
    next
  end

  version = File.read(VERSION_FILE).strip
  puts "Releasing version #{version}..."

  # 2. clojure -T:build release を実行
  system("clojure -T:build release") or abort("Release failed")

  puts "Release completed."
end

