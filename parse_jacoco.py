import xml.etree.ElementTree as ET
from pathlib import Path
import json

def parse_jacoco_xml(xml_path):
    """Parse JaCoCo XML and extract uncovered elements"""
    tree = ET.parse(xml_path)
    root = tree.getroot()
    
    uncovered = {
        'classes_not_tested': [],
        'methods_not_tested': [],
        'uncovered_branches': [],
        'uncovered_lines': []
    }
    
    for package in root.findall('.//package'):
        pkg_name = package.get('name', '')
        
        for cls in package.findall('.//class'):
            class_name = cls.get('name', '')
            source_file = cls.get('sourcefilename', '')
            
            # Check class-level coverage
            class_counters = {}
            for counter in cls.findall('counter'):
                counter_type = counter.get('type', '')
                missed = int(counter.get('missed', 0))
                covered = int(counter.get('covered', 0))
                class_counters[counter_type] = {'missed': missed, 'covered': covered}
            
            # Find uncovered methods
            for method in cls.findall('method'):
                method_name = method.get('name', '')
                method_desc = method.get('desc', '')
                method_line = method.get('line', '')
                
                method_missed = 0
                method_covered = 0
                branch_missed = 0
                branch_covered = 0
                
                for counter in method.findall('counter'):
                    counter_type = counter.get('type', '')
                    missed = int(counter.get('missed', 0))
                    covered = int(counter.get('covered', 0))
                    
                    if counter_type == 'INSTRUCTION':
                        method_missed = missed
                        method_covered = covered
                    elif counter_type == 'BRANCH':
                        branch_missed = missed
                        branch_covered = covered
                
                # Method is not tested at all
                if method_missed > 0 and method_covered == 0:
                    uncovered['methods_not_tested'].append({
                        'class': class_name,
                        'source': source_file,
                        'method': method_name,
                        'desc': method_desc,
                        'line': method_line,
                        'missed': method_missed
                    })
                
                # Method has uncovered branches
                if branch_missed > 0:
                    uncovered['uncovered_branches'].append({
                        'class': class_name,
                        'source': source_file,
                        'method': method_name,
                        'desc': method_desc,
                        'line': method_line,
                        'missed_branches': branch_missed,
                        'covered_branches': branch_covered
                    })
    
    return uncovered, class_counters

# Parse each module
modules = {
    'ms-vagas': r'C:\r\curriculum\ms-vagas\target\site\jacoco\jacoco.xml',
    'ms-curriculum': r'C:\r\curriculum\ms-curriculum\target\site\jacoco\jacoco.xml',
    'ms-usuario': r'C:\r\curriculum\ms-usuario\target\site\jacoco\jacoco.xml',
    'bff-curriculum': r'C:\r\curriculum\bff-curriculum\target\site\jacoco\jacoco.xml'
}

for module_name, xml_path in modules.items():
    if Path(xml_path).exists():
        uncovered, counters = parse_jacoco_xml(xml_path)
        print(f"\n{'='*60}")
        print(f"MODULE: {module_name}")
        print(f"{'='*60}")
        
        print(f"\n[NOT TESTED METHODS] ({len(uncovered['methods_not_tested'])} total)")
        for item in uncovered['methods_not_tested'][:10]:
            print(f"  - {item['class']}: {item['method']} (line {item['line']})")
        
        print(f"\n[UNCOVERED BRANCHES] ({len(uncovered['uncovered_branches'])} total)")
        for item in uncovered['uncovered_branches'][:10]:
            print(f"  - {item['class']}: {item['method']} ({item['missed_branches']} branches)")
