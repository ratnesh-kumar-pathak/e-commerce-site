import os, re
src_dir = r'd:\java\ShopKart\frontend\src'
for root, dirs, files in os.walk(src_dir):
    for file in files:
        if file.endswith('.jsx'):
            path = os.path.join(root, file)
            with open(path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            original_content = content
            
            # Replace localhost:8080 and localhost:9090
            content = content.replace('http://localhost:8080', '')
            content = content.replace('http://localhost:9090', '')
            
            # Replace fetch('/api/ and fetch("/api/
            content = re.sub(r'fetch\([\'"](/api/.*?)[\'"]', r'fetch((import.meta.env.VITE_API_URL || "") + "\1"', content)
            
            # Replace fetch('/admin/ and fetch("/admin/
            content = re.sub(r'fetch\([\'"](/admin/.*?)[\'"]', r'fetch((import.meta.env.VITE_API_URL || "") + "\1"', content)
            
            # Handle template literals like fetch(`/api/...`)
            content = re.sub(r'fetch\(`(/api/.*?)`', r'fetch(`${import.meta.env.VITE_API_URL || ""}\1`', content)
            content = re.sub(r'fetch\(`(/admin/.*?)`', r'fetch(`${import.meta.env.VITE_API_URL || ""}\1`', content)
            
            if content != original_content:
                with open(path, 'w', encoding='utf-8') as f:
                    f.write(content)
print('Done!')
